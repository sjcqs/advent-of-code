import { Chain, Interceptor } from "./interceptor"

interface AppIdHolder {
    api_app_id: string
}
export class VerifyAppId implements Interceptor {
    private appId: string
    
    constructor(appId: string) {
        this.appId = appId
    }

    async intercept(chain: Chain): Promise<void> {
        const body = chain.request.body
        const request: AppIdHolder = body
        if(request.api_app_id !== this.appId) {
            console.error("Invalid app id")
            chain.response.status(401).send()
            return
        }
        return chain.proceed(chain.request, chain.response)
    }

}
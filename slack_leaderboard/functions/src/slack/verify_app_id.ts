import { Chain, Interceptor } from "./interceptor"
import { EventRequest } from "./event_request"

export class VerifyAppId implements Interceptor {
    private appId: string
    
    constructor(appId: string) {
        this.appId = appId
    }

    async intercept(chain: Chain): Promise<void> {
        const request: EventRequest = chain.request.body
        if(request.api_app_id != this.appId) {
            console.error("Invalid app id")
            chain.response.status(401).send()
            return
        }
        return chain.proceed(chain.request, chain.response)
    }

}
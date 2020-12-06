import { Chain, Interceptor } from "./interceptor"

interface TokenHolder {
    token: string
}

export class VerifyToken implements Interceptor {
    private token: string
    
    constructor(token: string) {
        this.token = token
    }

    async intercept(chain: Chain): Promise<void> {
        const request: TokenHolder = chain.request.body
        if(request.token != this.token) {
            console.error("Invalid verification token")
            chain.response.status(401).send()
            return
        }
        return chain.proceed(chain.request, chain.response)
    }

}
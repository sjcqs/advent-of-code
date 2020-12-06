import { Chain, Interceptor } from "./interceptor";

const TYPE_URL_VERIFICATION = 'url_verification'

interface ChallengeRequest {
    token: string,
    challenge: string,
    type: string
}

export class CompleteChallenge implements Interceptor {
    
    async intercept(chain: Chain): Promise<void> {
        const request: ChallengeRequest = chain.request.body
        const response = chain.response
        if (request.type === TYPE_URL_VERIFICATION) {
            response.status(200).send(request.challenge)
            console.log("Challenge completed")
            return
        } 
        
        // no challenge, proceeding
        return chain.proceed(chain.request, response)
    }

}
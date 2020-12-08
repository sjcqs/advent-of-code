import { SlackConfig } from '../entity/configs';
import { Request, Response} from '../entity/types'
import { HomeManager } from './home_manager';
import { RealChain } from './interceptor';
import { Payload, PayloadActions, PayloadRequest } from './payload';
import { VerifyAppId } from './verify_app_id';
import { VerifySigningSecret } from './verify_signing_secret';


export class SlackPayloadClient {
    private homeManager: HomeManager
    private verifyAppId: VerifyAppId
    private verifySigningSecret: VerifySigningSecret

    constructor (config: SlackConfig, homeManager: HomeManager) {
        this.verifyAppId = new VerifyAppId(config.appId)
        this.verifySigningSecret = new VerifySigningSecret(config.signingSecret)
        this.homeManager = homeManager
    }

    async onRequest(request: Request, response: Response<any>) {
        const chain = new RealChain(
            request,
            response,
            0,
            [this.verifyAppId, this.verifySigningSecret],
            this.handleRequest.bind(this)
        )
        return chain.proceed(request, response)
    }

    private async handleRequest(request: Request, response: Response<any>) {
        response.status(200).send()
        const payloadRequest: PayloadRequest = request.body
        const payload: Payload = JSON.parse(payloadRequest.payload)
        
        if (payload.type === PayloadActions.refresh) {
            return this.homeManager.updateHome(payload.user.id, true)
        }
    }
}
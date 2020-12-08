import { SlackConfig } from '../entity/configs';
import { Request, Response} from '../entity/types'
import { HomeManager } from './home_manager';
import { RealChain } from './interceptor';
import { Payload, PayloadActions, PayloadRequest } from './payload';
import { VerifySigningSecret } from './verify_signing_secret';

const TYPE_BLOCK_ACTIONS = "block_actions"

export class SlackPayloadClient {
    private homeManager: HomeManager
    private verifySigningSecret: VerifySigningSecret

    constructor (config: SlackConfig, homeManager: HomeManager) {
        this.verifySigningSecret = new VerifySigningSecret(config.signingSecret)
        this.homeManager = homeManager
    }

    async onRequest(request: Request, response: Response<any>) {
        const chain = new RealChain(
            request,
            response,
            0,
            [this.verifySigningSecret],
            this.handleRequest.bind(this)
        )
        return chain.proceed(request, response)
    }

    private async handleRequest(request: Request, response: Response<any>) {
        response.status(200).send()
        const payloadRequest: PayloadRequest = request.body
        const payload: Payload = JSON.parse(payloadRequest.payload)
        
        console.log("payload: " + payload.type)
        if (payload.type === TYPE_BLOCK_ACTIONS) {
            console.log("payload: " + JSON.stringify(payload.actions))
            if (payload.actions[0].action_id === PayloadActions.refresh) {
                return this.homeManager.updateHome(payload.user.id, true)
            }
        }
    }
}
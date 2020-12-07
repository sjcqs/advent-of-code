import { SlackConfig } from '../entity/configs';
import { Request, Response} from '../entity/types'
import { CompleteChallenge } from './complete_challenge';
import { EventRequest } from './event_request';
import { HomeManager } from './home_manager';
import { RealChain } from './interceptor';
import { VerifyAppId } from './verify_app_id';
import { VerifySigningSecret } from './verify_signing_secret';
import { VerifyToken } from './verify_token';

const TYPE_CALLBACK = "event_callback"
const EVENT_OPENED_APP_HOME = "app_home_opened"

export class SlackEventClient {
    private homeManager: HomeManager
    private verifyAppId: VerifyAppId
    private verifyToken: VerifyToken
    private completeChallenge: CompleteChallenge
    private verifySigningSecret: VerifySigningSecret

    constructor (config: SlackConfig, homeManager: HomeManager) {
        this.verifyAppId = new VerifyAppId(config.appId)
        this.verifyToken = new VerifyToken(config.verificationToken)
        this.completeChallenge = new CompleteChallenge()
        this.verifySigningSecret = new VerifySigningSecret(config.signingSecret)
        this.homeManager = homeManager
    }

    async onRequest(request: Request, response: Response<any>) {
        const chain = new RealChain(
            request,
            response,
            0,
            [
                this.verifyToken,
                this.completeChallenge,
                this.verifyAppId,
                this.verifySigningSecret,
            ],
            this.handleRequest.bind(this)
        )
        return chain.proceed(request, response)
    }

    private async handleRequest(request: Request, response: Response<any>) {
        response.status(200).send()
        const model: EventRequest = request.body
        if (model.type === TYPE_CALLBACK &&
            model.event.type === EVENT_OPENED_APP_HOME
        ) {
            return this.homeManager.updateHome(model.event.user)
        }
    }
}
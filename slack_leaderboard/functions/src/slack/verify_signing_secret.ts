import { Chain, Interceptor } from "./interceptor"
import { Request, Response } from '../entity/types'
import {createHmac, Hmac} from 'crypto'

const SECRET_VERSION = "v0"

export class VerifySigningSecret implements Interceptor {
    
    constructor(private secret: string) {
    }

    async intercept(chain: Chain) {
        const rawBody = chain.request.rawBody.toString()
        const timestamp = this.timestamp(chain.request, chain.response)
        const requestSignature = chain.request.headers['x-slack-signature']
        if (timestamp === undefined || requestSignature === undefined) {
            console.error("Invalid headers")
            chain.response.status(401).send()
            return
        }

        const signature = this.signature(rawBody, timestamp)
        if (signature !== requestSignature) {
            console.error("Invalid signature")
            chain.response.status(401).send()
            return
        }
        return chain.proceed(chain.request, chain.response)
    }

    private timestamp(request: Request, response: Response<any>): number | undefined {
        const timestampHeader = request.headers['x-slack-request-timestamp']?.toString()
        if (timestampHeader === undefined) {
            console.error("Missing timestamp header")
            response.status(401).send()
            return undefined
        }
        const timestamp = Number.parseInt(timestampHeader)
        const currentTimestamp = Date.now() / 1000
        if (Math.abs(currentTimestamp - timestamp) > 60 * 5) {
            console.error("Outdated timestamp")
            response.status(401).send()
            return undefined
        }
        return timestamp
    }

    private signature(rawBody: string, timestamp: number): string {
        const hmac: Hmac = createHmac('sha256', this.secret)
        const baseSignature = `${SECRET_VERSION}:${timestamp}:${rawBody}`
        return `${SECRET_VERSION}=${hmac.update(baseSignature).digest('hex')}`
    }

}
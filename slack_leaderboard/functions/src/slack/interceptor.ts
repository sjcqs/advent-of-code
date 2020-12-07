import { Request, Response} from '../entity/types'

export interface Chain {
    request: Request,
    response: Response<any>,
    proceed(request: Request, response: Response<any>): Promise<void>
}

export interface Interceptor {
    intercept(chain: Chain): Promise<void>
}

export class RealChain implements Chain {
    request: Request
    response: Response<any>

    constructor(
        request: Request,
        response: Response<any>,
        private index: number,
        private interceptors: Interceptor[],
        private handleRequest: (request: Request, response: Response<any>) => Promise<void>
    ) {
        this.request = request
        this.response = response
    }

    copy(index : number = this.index): RealChain {
        return new RealChain(this.request, this.response, index, this.interceptors, this.handleRequest)
    }

    async proceed(request: Request, response: Response<any>): Promise<void> {
        if (this.index === this.interceptors.length) {
            return this.handleRequest(request, response)
        }

        const next = this.copy(this.index + 1)
        const interceptor = this.interceptors[this.index]

        return interceptor.intercept(next)
    }

}
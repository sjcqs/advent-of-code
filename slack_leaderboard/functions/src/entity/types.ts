import * as functions from 'firebase-functions';

export type Request = functions.https.Request
export type Response<T> = functions.Response<T>
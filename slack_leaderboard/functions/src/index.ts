import * as admin from 'firebase-admin';
import * as functions from 'firebase-functions';
import { AdventOfCodeApi } from './advent_of_code';
import { Config } from './config';
import { Database } from './database';
import { Leaderboard } from './entity/leaderboard';
import { SlackPublisher } from './slack/publisher';
import { Request, Response} from './entity/types'
import { SlackEventClient } from './slack/event_handler';

admin.initializeApp(functions.config().firebase);
const config = new Config(functions.config());
const slack = new SlackPublisher(config.slack)
const database = new Database(admin.database())
const api = new AdventOfCodeApi(config.adventOfCode)
const slackEventClient = new SlackEventClient(config.slack)

async function getAndPostLeaderboard() {
    const leaderboard: Leaderboard = await api.getLeaderboard()

    await database.putLeaderboard(leaderboard)
    await slack.publish(leaderboard)
}

async function handleSlackEvents(request: Request, response: Response<any>): Promise<void> {
    await slackEventClient.onRequest(request, response)
}

exports.scheduledFetchLeaderboard = functions.pubsub
    .schedule('0 14 * * *')
    .timeZone('Europe/Paris')
    .onRun(getAndPostLeaderboard)

exports.slackEvents = functions.https.onRequest(handleSlackEvents)
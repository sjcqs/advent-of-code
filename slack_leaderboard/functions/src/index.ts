import * as admin from 'firebase-admin';
import * as functions from 'firebase-functions';
import { AdventOfCodeApi } from './advent_of_code';
import { Config } from './config';
import { Database } from './database';
import { Leaderboard } from './entity/leaderboard';
import { SlackPublisher } from './slack/publisher';
import { Request, Response} from './entity/types'
import { SlackEventClient } from './slack/event_handler';
import { HomeManager } from './slack/home_manager';

admin.initializeApp(functions.config().firebase);
const config = new Config(functions.config());
const publisher = new SlackPublisher(config.slack)
const database = new Database(admin.database())
const api = new AdventOfCodeApi(config.adventOfCode)
const homeManager = new HomeManager(database, api, publisher)
const slackEventClient = new SlackEventClient(config.slack, homeManager)

async function getAndPostLeaderboard() {
    const leaderboard: Leaderboard = await api.getLeaderboard()

    await publisher.publish(leaderboard)
        .then(() => database.putLeaderboard(leaderboard))
}

async function handleSlackEvents(request: Request, response: Response<any>): Promise<void> {
    await slackEventClient.onRequest(request, response)
}

exports.scheduledFetchLeaderboard = functions.pubsub
    .schedule('0 14 * * *')
    .timeZone('Europe/Paris')
    .onRun(getAndPostLeaderboard)

exports.slackEvents = functions.https.onRequest(handleSlackEvents)
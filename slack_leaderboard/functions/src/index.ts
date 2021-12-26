import * as admin from 'firebase-admin';
import * as functions from 'firebase-functions';
import { AdventOfCodeApi } from './advent_of_code';
import { Config } from './config';
import { Database } from './database';
import { Leaderboard } from './entity/leaderboard';
import { SlackPublisher } from './slack/publisher';
import { SlackEventClient } from './slack/event_handler';
import { HomeManager } from './slack/home_manager';
import { SlackPayloadClient } from './slack/payload_handler';

admin.initializeApp(functions.config().firebase);
const config = new Config(functions.config());
const publisher = new SlackPublisher(config.slack)
const database = new Database(admin.database())
const api = new AdventOfCodeApi(config.adventOfCode)
const homeManager = new HomeManager(database, api, publisher)
const slackEventClient = new SlackEventClient(config.slack, homeManager)
const slackPayloadClient = new SlackPayloadClient(config.slack, homeManager)

async function getAndPostLeaderboard() {
    const leaderboard: Leaderboard = await api.getLeaderboard()
    const lastUpdate = await database.getLastUpdate()

    await publisher.publish(leaderboard, lastUpdate)
        .then(() => database.putLeaderboard(leaderboard))
}

exports.scheduledFetchLeaderboard = functions.pubsub
    .schedule('every day of december 14:00')
    .timeZone('Europe/Paris')
    .onRun(getAndPostLeaderboard)

exports.slackEvents = functions.https.onRequest(
    (request, response) => slackEventClient.onRequest(request, response)
)

exports.slackPayloads = functions.https.onRequest(
    (request, response) => slackPayloadClient.onRequest(request, response)
)
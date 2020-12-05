import * as admin from "firebase-admin";
import * as functions from 'firebase-functions';
import { AdventOfCodeApi } from "./advent_of_code";
import { Config } from "./config";
import { Database } from "./database";
import { Leaderboard } from "./entity/leaderboard";
import { Slack } from "./slack";

admin.initializeApp(functions.config().firebase);
const config = new Config(functions.config());
const slack = new Slack(config.slack)
const database = new Database(admin.database())
const api = new AdventOfCodeApi(config.adventOfCode)

async function getAndPostLeaderboard() {
    const leaderboard: Leaderboard = await api.getLeaderboard()

    await database.putLeaderboard(leaderboard)
    await slack.publish(leaderboard)
}

exports.scheduledFetchLeaderboard = functions.pubsub
    .schedule("0 14 * * *")
    .timeZone("Europe/Paris")
    .onRun(getAndPostLeaderboard)

exports.fetchLeaderboard = functions.https.onCall(getAndPostLeaderboard)
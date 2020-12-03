import * as admin from "firebase-admin";
import * as functions from 'firebase-functions';
import * as adventOfCode from './advent_of_code'
import * as config from './config'
import * as database from './database'
import { publishLeaderboard } from "./slack";

admin.initializeApp(functions.config().firebase);

exports.scheduledFetchLeaderboard = functions.pubsub.schedule("0 14 * * *")
.timeZone("Europe/Paris")
.onRun(async (context) => {
    const leaderboard = await adventOfCode.getLeaderboard(config.adventOfCode())
    await database.setLeaderboard(leaderboard)
    await publishLeaderboard(leaderboard, config.slack())
})

exports.fetchLeaderboard = functions.https.onCall(async (data, context) => {
    const leaderboard = await adventOfCode.getLeaderboard(config.adventOfCode())
    await database.setLeaderboard(leaderboard)
    await publishLeaderboard(leaderboard, config.slack())
  });
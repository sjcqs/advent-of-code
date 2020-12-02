import * as admin from "firebase-admin";
import { Leaderboard } from "./entity/leaderboard";

export async function setLeaderboard(leaderboard: Leaderboard) {
    return admin.database().ref("leaderboard").set(leaderboard)
}
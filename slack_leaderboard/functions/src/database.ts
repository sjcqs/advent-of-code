import * as admin from "firebase-admin";
import { Leaderboard } from "./entity/leaderboard";

export class Database {
    private database: admin.database.Database

    constructor(database: admin.database.Database) {
        this.database = database
    }

    async getLastUpdate(): Promise<Date> {
        return this.database.ref("last_update")
        .once('value')
        .then((snapshot) => new Date(snapshot.val()))
    }
    
    async putLeaderboard(leaderboard: Leaderboard) {
        return this.database.ref("leaderboard")
            .set(leaderboard)
            .then(() => this.putLastUpdate())
    }
    
    async getLeaderboard(): Promise<Leaderboard> {
        return this.database.ref("leaderboard")
        .once('value')
        .then((snapshot) => snapshot.val())
    }
    
    private async putLastUpdate(){
        return this.database.ref("last_update").set(new Date().getTime())
    }
}
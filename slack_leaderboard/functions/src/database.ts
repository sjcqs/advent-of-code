import * as admin from "firebase-admin";
import { Leaderboard } from "./entity/leaderboard";

export class Database {
    static Refs = {
        leaderboard: 'leaderboard',
        lastUpdate: 'lastUpdate',
    }

    private database: admin.database.Database

    constructor(database: admin.database.Database) {
        this.database = database
    }

    async getLastUpdate(): Promise<Date> {
        return this.database.ref(Database.Refs.lastUpdate)
            .once('value')
            .then((snapshot) => new Date(snapshot.val()))
    }
    
    async putLeaderboard(leaderboard: Leaderboard) {
        return this.database.ref(Database.Refs.leaderboard)
            .set(leaderboard)
            .then(() => this.putLastUpdate())
    }
    
    async getLeaderboard(): Promise<Leaderboard> {
        return this.database.ref(Database.Refs.leaderboard)
            .once('value')
            .then((snapshot) => snapshot.val())
    }
    
    private async putLastUpdate(){
        return this.database.ref(Database.Refs.lastUpdate)
            .set(new Date().getTime())
    }
}
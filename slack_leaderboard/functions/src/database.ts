import * as admin from "firebase-admin";
import { Leaderboard } from "./entity/leaderboard";

export class Database {
    static Refs = {
        leaderboard: 'leaderboard',
        lastUpdate: 'last_update',
        home: 'home',
    }

    private database: admin.database.Database

    constructor(database: admin.database.Database) {
        this.database = database
    }

    async getLastUpdate(): Promise<number> {
        return this.database.ref(Database.Refs.lastUpdate)
            .once('value')
            .then((snapshot) => snapshot.val())
    }

    async putHomeUpdate(userId: string, update: number) {
        return this.database.ref(Database.Refs.home)
            .child(userId)
            .set(update)
    }

    async getHomeLastUpdate(userId: string): Promise<number> {
        return this.database.ref(Database.Refs.home)
            .child(userId)
            .once('value')
            .then((snapshot) => snapshot.val())
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
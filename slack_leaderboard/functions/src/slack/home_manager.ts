import { AdventOfCodeApi } from "../advent_of_code";
import { Database } from "../database";
import { SlackPublisher } from "./publisher";

export class HomeManager {
    private database: Database
    private api: AdventOfCodeApi
    private publisher: SlackPublisher

    constructor(database: Database, api: AdventOfCodeApi, publisher: SlackPublisher) {
        this.database = database
        this.api = api
        this.publisher = publisher
    }

    async updateHome(userId: string, refresh: boolean = false) {
        const homeLastUpdate = await this.database.getHomeLastUpdate(userId)
        let lastUpdate = await this.database.getLastUpdate()

        if (refresh) {
            const leaderboard = this.api.getLeaderboard()
                .then((newLeaderboard) => this.database.putLeaderboard(newLeaderboard))
            lastUpdate = await this.database.getLastUpdate()
            return this.publisher.updateHome(
                userId,
                await leaderboard,
                lastUpdate
            ).then(() => this.database.putHomeUpdate(userId, lastUpdate))
        } else {
            const leaderboard = this.database.getLeaderboard()
            if (homeLastUpdate === null || homeLastUpdate < lastUpdate) {
                return this.publisher.updateHome(
                    userId,
                    await leaderboard,
                    lastUpdate
                ).then(() => this.database.putHomeUpdate(userId, lastUpdate))
            }
        }
    }
}
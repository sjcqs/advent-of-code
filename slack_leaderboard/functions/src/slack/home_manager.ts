import { AdventOfCodeApi } from "../advent_of_code";
import { Database } from "../database";
import { Leaderboard } from "../entity/leaderboard";
import { SlackPublisher } from "./publisher";

const MAX_REFRESH_RATE = 15 * 60 * 1000

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

        let leaderboard: Promise<Leaderboard>
        const shouldRefresh = lastUpdate === null ||
            (refresh && lastUpdate - homeLastUpdate > MAX_REFRESH_RATE)
        if (shouldRefresh) {
            leaderboard = this.api.getLeaderboard()
                .then(this.database.putLeaderboard.bind(this))
        } else {
            leaderboard = this.database.getLeaderboard()
        }
        if (homeLastUpdate === null || homeLastUpdate < lastUpdate) {
            lastUpdate = await this.database.getLastUpdate()
            return this.publisher.updateHome(
                userId,
                await leaderboard,
                lastUpdate
            ).then(() => this.database.putHomeUpdate(userId, lastUpdate))
        }
    }
}
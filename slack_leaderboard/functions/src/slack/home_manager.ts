import { AdventOfCodeApi } from "../advent_of_code";
import { Database } from "../database";
import { Leaderboard } from "../entity/leaderboard";
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

    async updateHome(userId: string) {
        const homeLastUpdate = await this.database.getHomeLastUpdate(userId)
        const lastUpdate = await this.database.getLastUpdate()

        let leaderboard: Promise<Leaderboard>
        if (lastUpdate === null) {
            leaderboard = this.api.getLeaderboard()
                .then(this.database.putLeaderboard.bind(this))
        } else {
            leaderboard = this.database.getLeaderboard()
        }
        if (homeLastUpdate === null || homeLastUpdate < lastUpdate) {
            return this.publisher.updateHome(userId, await leaderboard)
                .then(() => this.database.putHomeUpdate(userId))
        }
    }
}
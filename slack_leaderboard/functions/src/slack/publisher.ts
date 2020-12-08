import Axios from "axios";
import { SlackConfig } from "../entity/configs";
import { Leaderboard } from "../entity/leaderboard";
import { MessageBuilder } from "./message_builder";

const URL_PUBLISH_HOME = "https://slack.com/api/views.publish"

export class SlackPublisher {
    private config: SlackConfig
    private messageBuilder: MessageBuilder

    constructor(config: SlackConfig) {
        this.config = config
        this.messageBuilder = new MessageBuilder()
    }

    async publish(leaderboard: Leaderboard) {
        const blocks = this.messageBuilder.leaderboardBlocks(leaderboard)
        const payload = {
            "username": "Advent of Code",
            "icon_emoji": ":aoc:",
            blocks,
        }
        return Axios.post(this.config.hookUrl, payload)
    }

    async updateHome(userId: string, leaderboard: Leaderboard) {
        const blocks = this.messageBuilder.leaderboardBlocks(leaderboard)
        const payload = {
            "user_id": userId,
            "view": {
                "type": "home",
                "blocks": blocks,
            },
        }
        const config = { 
            headers: { 
                "Authorization": `Bearer ${this.config.botToken}`, 
            },
        };
        return Axios.post(URL_PUBLISH_HOME, payload, config)
    }
}
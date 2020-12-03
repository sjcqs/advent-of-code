import Axios from "axios";
import { SlackConfig } from "./entity/configs";
import { Leaderboard } from "./entity/leaderboard";
import { MessageBuilder } from "./message_builder";

export class Slack {
    private config: SlackConfig
    private messageBuilder: MessageBuilder

    constructor(config: SlackConfig) {
        this.config = config
        this.messageBuilder = new MessageBuilder()
    }

    async publish(leaderboard: Leaderboard) {
        const payload = this.messageBuilder.leaderboard(leaderboard)
        await Axios.post(this.config.hookUrl, payload)
    }
}
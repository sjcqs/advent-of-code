import { config } from 'firebase-functions';
import { AdventOfCodeConfig, SlackConfig } from './entity/configs';

const ADVENT_OF_CODE_KEY = "adventofcode"
const SLACK_KEY = "slack"

export class Config {
    adventOfCode: AdventOfCodeConfig
    slack: SlackConfig

    constructor(config: config.Config) {
        this.adventOfCode = this.buildAdventOfCodeConfig(config)
        this.slack = this.buildSlackConfig(config)
    }

    private buildAdventOfCodeConfig(config: config.Config): AdventOfCodeConfig {
        const adventOfCodeConfig = config[ADVENT_OF_CODE_KEY]
        const session = adventOfCodeConfig.session
        const url = adventOfCodeConfig.url + ".json"
    
        return { url: url, sessionValue: session }
    }

    private buildSlackConfig(config: config.Config): SlackConfig {
        const slackConfig = config[SLACK_KEY]
        return { hookUrl: slackConfig.incomminghook, channel: slackConfig.channel }
    }

}
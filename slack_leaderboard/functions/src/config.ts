import { config } from 'firebase-functions';
import { AdventOfCodeConfig, SlackConfig } from './entity/configs';

const ON_EMULATOR = process.env.FUNCTIONS_EMULATOR === 'true'
const ADVENT_OF_CODE_KEY = "adventofcode"
const SLACK_KEY = "slack"

export class Config {
    adventOfCode: AdventOfCodeConfig
    slack: SlackConfig

    constructor(firebaseConfig: config.Config) {
        this.adventOfCode = this.buildAdventOfCodeConfig(firebaseConfig)
        this.slack = this.buildSlackConfig(firebaseConfig)
    }

    private buildAdventOfCodeConfig(firebaseConfig: config.Config): AdventOfCodeConfig {
        const adventOfCodeConfig = firebaseConfig[ADVENT_OF_CODE_KEY]
        const session = adventOfCodeConfig['session']
        const url = adventOfCodeConfig['url'] + ".json"
    
        return { url: url, sessionValue: session }
    }

    private buildSlackConfig(firebaseConfig: config.Config): SlackConfig {
        const slackConfig = firebaseConfig[SLACK_KEY]
        let incommingHook: string
        if (ON_EMULATOR) {
            incommingHook = slackConfig['incominghook_dev']
        } else {
            incommingHook = slackConfig['incominghook']
        }
        return { 
            hookUrl: incommingHook,
            botToken: slackConfig['bot_token'],
            signingSecret: slackConfig['signing_secret'],
            appId: slackConfig['app_id'],
            verificationToken: slackConfig['verification_token']
        }
    }

}
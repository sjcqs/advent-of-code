import * as functions from 'firebase-functions';
import { AdventOfCodeConfig, SlackConfig } from './entity/configs';

const ADVENT_OF_CODE_KEY = "adventofcode"
const SLACK_KEY = "slack"

const config = functions.config()

export function adventOfCode(): AdventOfCodeConfig {
    const adventOfCodeConfig = config[ADVENT_OF_CODE_KEY]
    const session = adventOfCodeConfig.session
    const url = adventOfCodeConfig.url + ".json"

    return { url: url, sessionValue: session }
}

export function slack(): SlackConfig {
    const slackConfig = config[SLACK_KEY]
    return { hookUrl: slackConfig.incomminghook, channel: slackConfig.channel }
}
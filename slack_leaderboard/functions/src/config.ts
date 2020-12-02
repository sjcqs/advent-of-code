import * as functions from 'firebase-functions';
import { AdventOfCodeConfig, SlackConfig } from './entity/configs';

const ADVENT_OF_CODE_KEY = "adventofcode"
const SLACK_KEY = "slack"

const config = functions.config()

export function adventOfCode(): AdventOfCodeConfig {
    const session = config[ADVENT_OF_CODE_KEY].session
    const url = config[ADVENT_OF_CODE_KEY].url + ".json"

    return { url: url, sessionValue: session }
}

export function slack(): SlackConfig {
    return { hookUrl: config[SLACK_KEY].incomminghook, channel: config[SLACK_KEY].channel }
}
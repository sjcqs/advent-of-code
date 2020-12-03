import Axios from 'axios'
import {Leaderboard, toLeaderboard} from './entity/leaderboard'
import { AdventOfCodeConfig } from './entity/configs';

export async function getLeaderboard(config: AdventOfCodeConfig): Promise<Leaderboard> {
    return Axios
        .get(config.url, {headers: {Cookie: `session=${config.sessionValue}`}, withCredentials: true})
        .then(response => { return toLeaderboard(response.data) })
        .catch(error => { throw new Error(`${error}`) });
}
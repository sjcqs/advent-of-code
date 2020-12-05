import Axios from 'axios'
import { Leaderboard } from './entity/leaderboard'
import { AdventOfCodeConfig } from './entity/configs';
import { Day, Member } from './entity/member';


interface RestLeaderboard {
    owner_id: string;
    event: string;
    members: RestMembers;
}

interface RestMembers {
    [id: string]: RestMember
}

interface RestMember {
    stars: number;
    last_star_ts: number;
    global_score: number;
    id: string;
    local_score: number;
    name: string;
    completion_day_level: RestDays
}

interface RestDays {
    [day: string]: RestDay
}

interface RestDay {
    [level: string]: RestStarTs
}

interface RestStarTs {
    get_star_ts: string
}

export class AdventOfCodeApi {
    private config: AdventOfCodeConfig
    private mapper: AdventofCodeRestMapper

    constructor(config: AdventOfCodeConfig) {
        this.config = config
        this.mapper = new AdventofCodeRestMapper()
    }
    
    async getLeaderboard(): Promise<Leaderboard> {
        const config = { 
            headers: { Cookie: `session=${this.config.sessionValue}` }, 
            withCredentials: true,
        }
        return Axios.get(this.config.url, config)
            .then(response => response.data)
            .then((leaderboard) => this.mapper.mapLeaderboard(leaderboard))
    }
}

class AdventofCodeRestMapper {

    mapLeaderboard (toMap: RestLeaderboard): Leaderboard {
        const restMembers = Object.values(toMap.members);
        const members = this.mapMembers(restMembers);
        return {
            ownerId: toMap.owner_id,
            event: toMap.event,
            members,
        }
    }

    mapMembers(members: RestMember[]): Member[] {
        return members.map(this.mapMember.bind(this));
    }
    
    mapMember(model: RestMember): Member {
        const days: Day[] = this.mapDays(model);
        return {
            id: model.id,
            name: model.name,
            stars: model.stars,
            lastStarTs: model.last_star_ts,
            localScore: model.local_score,
            days: days,
        }
    }
    
    mapDays(model: RestMember) {
        const days: Day[] = [];
        for (let index = 1; index <= 24; index++) {
            const key = `${index}`;
            if (key in model.completion_day_level) {
                const day = model.completion_day_level[key];
                days.push({ hasFirstStar: "1" in day, hasSecondStar: "2" in day });
            } else {
                days.push({ hasFirstStar: false, hasSecondStar: false });
            }
        }
        return days;
    }
}
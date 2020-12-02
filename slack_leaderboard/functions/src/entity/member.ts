export interface RestMember {
    stars: number;
    last_star_ts: string;
    global_score: number;
    id: string;
    local_score: number;
    name: string;
    completion_day_level: RestDays
}

export interface RestDays {
    [day: string]: RestDay
}

export interface RestDay {
    [level: string]: RestStarTs
}

export interface RestStarTs {
    get_star_ts: string
}

export function toMember(model: RestMember): Member {
    const days: Day[] = []
    for (let index = 1; index <= 24; index++) {
        console.log("Member: " + model)
        const key = `${index}`
        if (key in model.completion_day_level) {
            const day = model.completion_day_level[key];
            days.push({ hasFirstStar: "1" in day, hasSecondStar: "2" in day })
        } else {
            days.push({hasFirstStar: false, hasSecondStar: false})
        }
    }
    return {
        id: model.id,
        name: model.name,
        stars: model.stars,
        lastStarTs: model.last_star_ts,
        localScore: model.local_score,
        days: days,
    }
}

export interface Member {
    stars: number;
    lastStarTs: string;
    id: string;
    localScore: number;
    name: string;
    days: Day[]
}

export interface Day {
    hasFirstStar: boolean;
    hasSecondStar: boolean;
}
  
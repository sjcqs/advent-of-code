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
  
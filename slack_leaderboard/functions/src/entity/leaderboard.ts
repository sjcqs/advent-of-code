import { Member } from "./member";
import { RestMembers } from "./members";

export interface RestLeaderboard {
    owner_id: string;
    event: string;
    members: RestMembers;
}

export interface Leaderboard {
    owner_id: string;
    event: string;
    members: Member[];
}

export function toLeaderboard(toMap: RestLeaderboard): Leaderboard {
    return {
        owner_id: toMap.owner_id,
        event: toMap.event,
        members: Object.values(toMap.members),
    }
}
  
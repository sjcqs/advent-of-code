import { Member, toMember } from "./member";
import { RestMembers } from "./members";

export interface RestLeaderboard {
    owner_id: string;
    event: string;
    members: RestMembers;
}

export interface Leaderboard {
    ownerId: string;
    event: string;
    members: Member[];
}

export function toLeaderboard(toMap: RestLeaderboard): Leaderboard {
    return {
        ownerId: toMap.owner_id,
        event: toMap.event,
        members: Object.values(toMap.members).map(toMember),
    }
}
  
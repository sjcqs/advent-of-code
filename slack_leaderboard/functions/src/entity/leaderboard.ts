import { Member } from "./member";

export interface Leaderboard {
    ownerId: string;
    event: string;
    members: Member[];
}

  
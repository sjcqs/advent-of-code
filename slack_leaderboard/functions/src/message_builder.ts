import { Leaderboard } from "./entity/leaderboard";
import { Member } from "./entity/member";

export class MessageBuilder {
    private header = {
        "type": "header",
        "text": {
            "type": "plain_text",
            "text": "Leaderboard",
            "emoji": true,
        },
    }
    private divider = { "type": "divider"}
    
    private adventOfCodeLink = {
        "type": "section",
        "text": {
            "type": "mrkdwn",
            "text": ":brain: Resolve the next puzzle",
        },
        "accessory": {
            "type": "button",
            "text": {
                "type": "plain_text",
                "text": "Advent of code",
                "emoji": true,
            },
            "value": "advent_of_code",
            "url": "https://adventofcode.com/2020",
            "action_id": "button-action",
        },
    }

    leaderboard(leaderboard: Leaderboard) {
        const members = this.sortMembers(leaderboard)
        const blocks = [
            this.header,
            this.divider,
            this.firstRankBlock(members[0]),
            this.secondRankBlock(members[1]),
            this.thirdRankBlock(members[2]),
        ]
        for (let index = 3; index < members.length; index++) {
            const member = members[index];
            blocks.push(this.otherRankBlock(member, index + 1))
        }
        blocks.push(this.divider, this.adventOfCodeLink, this.updateBlock())
        return {
            "username": "Advent of Code",
            "icon_emoji": ":aoc:",
            "blocks": blocks,
        }
    }
    
    private updateBlock() {
        return {
            "type": "context",
            "elements": [
                {
                    "type": "plain_text",
                    "text": `Updated at ${new Date().toLocaleString("fr-FR")}`,
                    "emoji": false,
                },
            ],
        }
    }
    
    private rankBlock(icon: string, position: number, member: Member) {
        return {
            "type": "section",
            "text": {
                "type": "plain_text",
                "text": `${icon} ${position} - ${member.name}: ${member.localScore} (${member.stars} :star:)`,
                "emoji": true,
            },
        }
    }
    
    
    private firstRankBlock(member: Member) {
        return this.rankBlock(":first_place_medal:", 1, member)
    }
    
    private secondRankBlock(member: Member) {
        return this.rankBlock(":second_place_medal:", 2, member)
    }
    
    private thirdRankBlock(member: Member) {
        return this.rankBlock(":third_place_medal:", 3, member)
    }
    
    private otherRankBlock(member: Member, position: number) {
        return this.rankBlock(":man-amish-technologist:", position, member)
    }

    private sortMembers(leaderboard: Leaderboard) {
        return leaderboard.members
            .filter((member) => member.stars > 0)
            .sort((firstMember, secondMember) => (secondMember.localScore - firstMember.localScore));
    }
}
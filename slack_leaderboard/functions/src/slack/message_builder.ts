import { Leaderboard } from "../entity/leaderboard";
import { Member } from "../entity/member";
import { PayloadActions } from "./payload";

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
            "action_id": PayloadActions.adventOfCode,
        },
    }

    private refreshBlock = {
        "type": "section",
        "text": {
            "type": "mrkdwn",
            "text": "Refresh the leaderboard.",
        },
        "accessory": {
            "type": "button",
            "text": {
                "type": "plain_text",
                "text": "Refresh",
                "emoji": true,
            },
            "action_id": PayloadActions.refresh,
        },
    }
	private refreshRateBlock =	{
		"type": "context",
		"elements": [
			{
				"type": "plain_text",
				"text": ":information_source: The leaderboard can only be refreshed every 15 minutes.",
				"emoji": true,
			},
		],
	}

    leaderboardBlocks(
        leaderboard: Leaderboard,
        lastUpdate: number,
        addRefreshButton: boolean = false
    ) {
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
        if (addRefreshButton) {
            blocks.push(
                this.divider,
                this.refreshBlock,
                this.refreshRateBlock,
                this.updateBlock(lastUpdate),
                this.divider,
                this.adventOfCodeLink,
            )
        } else {
            blocks.push(
                this.divider,
                this.adventOfCodeLink,
                this.updateBlock(lastUpdate),
            )   
        }
        return blocks
    }
    
    private updateBlock(lastUpdate: number) {
        return {
            "type": "context",
            "elements": [
                {
                    "type": "plain_text",
                    "text": `Updated at ${new Date(lastUpdate).toLocaleString()}`,
                    "emoji": false,
                },
            ],
        }
    }
    
    private rankBlock(icon: string, position: number, member: Member) {
        const name = member.name;
        const score = member.localScore;
        const stars = `${member.stars} :star:`
        const text = `${icon} ${position} - ${name}: ${score} (${stars})`;
        return {
            "type": "section",
            "text": {
                "type": "plain_text",
                "text": text,
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
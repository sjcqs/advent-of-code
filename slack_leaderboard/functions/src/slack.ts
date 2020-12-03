import Axios from "axios";
import { SlackConfig } from "./entity/configs";
import { Leaderboard } from "./entity/leaderboard";
import { Day, Member } from "./entity/member";

const header = {
    "type": "header",
    "text": {
        "type": "plain_text",
        "text": "Leaderboard",
        "emoji": true,
    },
}
const divider = { "type": "divider"}

const adventOfCodeLink = {
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

function updateBlock() {
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

function rankBlock(icon: string, position: number, member: Member) {
    return {
        "type": "section",
        "text": {
            "type": "plain_text",
            "text": `${icon} ${position} - ${member.name}: ${member.localScore}`,
            "emoji": true,
        },
    }
}

function starsBlock(total: number,days: Day[]) {
    let stars = ""
    for (const day of days) {
        if (day.hasFirstStar && day.hasSecondStar) {
            stars += ":star: "
        } else if(day.hasFirstStar){
            stars += ":silver_star:"
        }
    }
    stars += `(${total})`
    return {
        "type": "context",
        "elements": [
            {
                "type": "plain_text",
                "text": stars,
                "emoji": true,
            },
        ],
    }
}
function firstRankBlock(member: Member) {
    return rankBlock(":first_place_medal:", 1, member)
}

function secondRankBlock(member: Member) {
    return rankBlock(":second_place_medal:", 2, member)
}

function thirdRankBlock(member: Member) {
    return rankBlock(":third_place_medal:", 3, member)
}

function otherRankBlock(member: Member, position: number) {
    return rankBlock(":man-amish-technologist:", position, member)
}

export async function publishLeaderboard(leaderboard:Leaderboard, slackConfig: SlackConfig) {
    const payload = buildPayload(leaderboard)
    await Axios.post(slackConfig.hookUrl, payload)
}

function buildPayload(leaderboard: Leaderboard) {
    const members = leaderboard.members
        .filter((member) => member.stars > 0)
        .sort((firstMember, secondMember) => (secondMember.localScore - firstMember.localScore))
    const blocks = [
        header,
        divider,
        firstRankBlock(members[0]),
        starsBlock(members[0].stars, members[0].days),
        secondRankBlock(members[1]),
        starsBlock(members[1].stars, members[1].days),
        thirdRankBlock(members[2]),
        starsBlock(members[2].stars, members[2].days),
    ]
    for (let index = 3; index < members.length; index++) {
        const member = members[index];

        blocks.push(otherRankBlock(member, index + 1), starsBlock(member.stars, member.days),)
    }
    blocks.push(divider, adventOfCodeLink, updateBlock())
    return {
        "username": "Advent of Code",
        "icon_emoji": ":aoc:",
        "blocks": blocks,
    }
}
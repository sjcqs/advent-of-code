export interface PayloadRequest {
    payload: string
}

export interface User {
    id: string
}

export interface Text {
    type: string
    text: string
    emoji: boolean
}

export interface PayloadAction {
    action_id: string
    block_id: string
    text: Text
    value: string
    type: string
    action_ts: string
}

export interface Payload {
    type: string
    user: User
    api_app_id: string
    actions: PayloadAction[]
}
export const PayloadTypes  = {
    blockActions: "block_actions",
}
export const PayloadActions = {
    refresh: "button-leaderboard-refresh",
    adventOfCode: "button-advent-of-code",
}

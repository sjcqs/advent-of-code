export interface EventRequest {
    token:          string;
    team_id:        string;
    api_app_id:     string;
    event:          Event;
    type:           string;
    authed_users:   string[];
    authorizations: Authorizations;
    event_context:  string;
    event_id:       string;
    event_time:     number;
}

export interface Authorizations {
    enterprise_id: string;
    team_id:       string;
    user_id:       string;
    is_bot:        boolean;
}

export interface Event {
    type:     string;
    event_ts: string;
    user:     string;
}
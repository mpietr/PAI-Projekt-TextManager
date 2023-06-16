import { User } from "./user";

export interface TextResource {
    name: string,
    ownerId: User,
    text: string,
    tags: string,
    code: string
}
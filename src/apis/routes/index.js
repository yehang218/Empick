import { AuthAPI } from './auth';
import { MemberAPI } from './member';
import { FileAPI } from './file';
import { MailAPI } from './mail';

export const API = {
    AUTH: AuthAPI,
    MEMBER: MemberAPI,
    FILE: FileAPI,
    MAIL: MailAPI,
};

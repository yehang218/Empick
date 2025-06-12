import { AuthAPI } from './auth';
import { JobtestAPI } from './jobtest';
import { MemberAPI } from './member';
import { FileAPI } from './file';
import { MailAPI } from './mail';
import { RecruitmentAPI } from './recruitment';

export const API = {
    AUTH: AuthAPI,
    JOBTEST: JobtestAPI,
    MEMBER: MemberAPI,
    FILE: FileAPI,
    MAIL: MailAPI,
    RECRUITMENT: RecruitmentAPI
};

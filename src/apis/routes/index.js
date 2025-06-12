import { AuthAPI } from './auth';
import { JobtestAPI } from './jobtest';
import { InterviewAPI } from './interview';
import { MemberAPI } from './member';
import { FileAPI } from './file';
import { MailAPI } from './mail';


export const API = {
    AUTH: AuthAPI,
    JOBTEST: JobtestAPI,
    INTERVIEW: InterviewAPI,
    MEMBER: MemberAPI,
    FILE: FileAPI,
    MAIL: MailAPI,
};

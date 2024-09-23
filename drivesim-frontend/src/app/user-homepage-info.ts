
export class UserHomepageInfo {
    firstName: string;
    lastName: string;
    gems: number;
    
    constructor(firstName: string, lastName: string, gems: number) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gems = gems;
    }  
}
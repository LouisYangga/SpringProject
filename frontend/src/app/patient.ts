export interface Patient{
    id:number;
    firstName: string;
    lastName: string;
    gender: string;
    dob: string;
    phoneNumber: number;
    address:{
        address: string;
        suburb: string;
        state: string;
        postcode: number;
    }
}
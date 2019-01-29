import { ContractResource } from "./contractResource";

export class Contract {
    id: number;
    sender: Person;
    receiver: Person;

    conclusionDate: string;
    contractResources: ContractResource[];
    token: string; // this token is used to accept / reject a contract

}
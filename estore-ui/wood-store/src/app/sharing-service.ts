import { Injectable } from '@angular/core';

/**
 * used to pass current user to sibling components
 */
@Injectable()
export class SharingService{
    private data:any = undefined;

    setData(data:any){
        this.data = data;
    }

    getData():any{
        return this.data;
    }
}
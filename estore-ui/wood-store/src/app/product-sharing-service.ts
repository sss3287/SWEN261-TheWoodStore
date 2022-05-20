import { Injectable } from '@angular/core';

/**
 * used to pass inventory to sibling components
 */
@Injectable()
export class ProductSharingService{
    private data:any = undefined;

    setData(data:any){
        this.data = data;
    }

    getData():any{
        return this.data;
    }
}
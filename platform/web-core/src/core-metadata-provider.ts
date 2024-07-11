export type HTTPHeaders = { [key: string]: string };
export type HTTPMethod = 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE' | 'OPTIONS' | 'HEAD';

export type Configuration = {
    basePath?: string; // override base path
    headers?: HTTPHeaders; //header params we want to use on every request
}
export type BaseElementWithId = {
    id: string
}


export type StandardValueType = 'STRING' | 'LOCAL_DATE' | 'LOCAL_DATE_TIME' | 'ENUM'
    | 'BOOLEAN' | 'ENTITY_REFERENCE' | 'LONG' | 'INT' | 'BIG_DECIMAL' | 'INSTANT' | 'UUID'|'ENTITY';

export type L10nMessageParameterDescription = BaseElementWithId & {
    type: StandardValueType,
    collection: boolean
}

export type ServiceDescription = {
    requestClassName?: string;
    responseClassName?: string
    method: HTTPMethod
    path?: string;
    multipartRequest: boolean
}
export type EntityPropertyDescription = BaseElementWithId &{
    type: StandardValueType,
    className?: string
}

export type EntityCollectionDescription = BaseElementWithId &{
    elementType: StandardValueType,
    elementClassName?: string
}
export type EntityMapDescription = BaseElementWithId &{
    keyType: StandardValueType,
    keyClassName?: string
    valueType: StandardValueType,
    valueClassName?: string
}

export type EntityDescription = {
    isAbstract: boolean
    properties?:EntityPropertyDescription[]
    collections?:EntityCollectionDescription[]
    maps?: EntityMapDescription[]
}
export type L10nMessageDescription = {
    id: string,
    displayName: string,
    parameters: L10nMessageParameterDescription[],
};

export type L10nMessagesBundleDescription = BaseElementWithId & {
    messages: Map<String, L10nMessageDescription>
}

const services = new Map<string, ServiceDescription>()
const entities = new Map<string, EntityDescription>()

const request = async (configuration: Configuration, relativeUrl: string) => {
    const headers = Object.assign({}, configuration.headers || {});
    const initParams = {
        method: 'GET',
        headers
    } as RequestInit;
    const url = `${configuration.basePath}/${relativeUrl}`
    const response = await fetch(url, initParams)
    return await response.json()
}

export async function getServiceDescription(configuration: Configuration, remotingId: string, groupId: string, serviceId: string) {
    const id = `${remotingId}/${groupId}/${serviceId}`
    if(services.has(id)){
        return services.get(id)!!
    }
    const description = (await request(configuration, `core/meta/service-description?remotingId=${remotingId}&groupId=${groupId}&serviceId=${serviceId}`)) as ServiceDescription
    services.set(id, description)
    return description!!
}


export async function getEntityDescription(configuration: Configuration, entityId:string) {
    if(entities.has(entityId)){
        return entities.get(entityId)!!
    }
    const description = (await request(configuration, `core/meta/entity-description?entityId=${entityId}`)) as EntityDescription
    entities.set(entityId, description)
    return description!!
}
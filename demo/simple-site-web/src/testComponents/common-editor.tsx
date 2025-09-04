import React, { createContext, useContext, useState } from 'react';
import { useRouter } from './common-router';

export interface EditorContextType {
    hasChanges: boolean;
    setHasChanges: (value: boolean) => void;
}

const EditorContext = createContext<EditorContextType | undefined>(undefined);

export const useEditor = (): EditorContextType | undefined => {
    return useContext(EditorContext);
};

export interface Editor {
    isHasChanges: () => boolean;
    setHasChanges: (value: boolean) => void;
    stateSetters: Map<string, any | null | undefined>;
    getTitle(): string;
    onBackButtonPressed: () => void;
    onSaveButtonPressed: () => void;
    isDataLoading: () => boolean;
}
export function EditorWrapper(props: React.PropsWithChildren<{ element: Editor }>) {
    const [hasChanges, setHasChanges] = useState<boolean>(props.element.isHasChanges());
    props.element.stateSetters.set('hasChanges', setHasChanges);
    const [title, setTitle] = useState<string>(props.element.getTitle());
    props.element.stateSetters.set('title', setTitle);
    const [dataLoading, setDataLoading] = useState<boolean>(
        props.element.isDataLoading()
    );
    props.element.stateSetters.set('dataLoading', setDataLoading);
    const router = useRouter();
    return (
        <EditorContext.Provider
            value={{
                hasChanges,
                setHasChanges: (value: boolean) => {
                    props.element.setHasChanges(value);
                    if (router) {
                        router.setHasChanges(true);
                    }
                },
            }}
        >
            {dataLoading ? (
                'Data is loading'
            ) : (
                <div className="webpeer-container">
                    <button
                        className="webpeer-button"
                        onClick={() => {
                            props.element.onBackButtonPressed();
                        }}
                    >
                        Back
                    </button>
                    <div>{title}</div>
                    <button
                        disabled={!hasChanges}
                        className="webpeer-button"
                        onClick={() => {
                            props.element.onSaveButtonPressed();
                        }}
                    >
                        Save
                    </button>
                    {props.children}
                </div>
            )}
        </EditorContext.Provider>
    );
}

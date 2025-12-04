import { BaseReactUiElement, initStateSetters } from 'admin/src/common/component';
import { EntityEditorSkeleton } from 'admin/src-gen/entityEditor/EntityEditorSkeleton';
import { theme } from 'antd';
import { createContext, useContext } from 'react';

export interface EditorContextType {
    hasTag: (tag: string) => boolean;
    addTag: (tag: string) => void;
    removeTag: (tag: string) => void;
}

export const EditorContext = createContext<EditorContextType | undefined>(undefined);

export const useEditor = (): EditorContextType | undefined => {
    return useContext(EditorContext);
};

function EntityEditorFC(props: { element: EntityEditorComponent }) {
    initStateSetters(props.element);
    const { token } = theme.useToken();
    const setTags = (tags: string[]) => {
        props.element.stateSetters.get('tags')!(tags);
        props.element.sendCommand(
            'pc',
            {
                pn: 'tags',
                pv: tags,
            },
            true
        );
    };
    const editorContext = {
        addTag: (tag: string) => {
            const tags = [...(props.element.getTags() || [])];
            if (tags.indexOf(tag) === -1) {
                tags.push(tag);
            }
            setTags(tags);
        },
        removeTag: (tag: string) => {
            let tags = [...(props.element.getTags() || [])];
            const idx = tags.indexOf(tag);
            if (idx !== -1) {
                tags = tags.splice(idx, 1);
            }
            setTags(tags);
        },
        hasTag: (tag: string) => {
            return (props.element.getTags() || []).indexOf(tag) !== -1;
        },
    } as EditorContextType;

    return (
        <EditorContext.Provider value={editorContext}>
            <div
                style={{
                    display: 'flex',
                    flexDirection: 'column',
                    flexGrow: 1,
                    margin: token.paddingXXS,
                }}
            >
                <div
                    style={{
                        display: 'flex',
                        flexDirection: 'row',
                        flexGrow: 0,
                        flexShrink: 0,
                    }}
                >
                    {props.element.findByTag('tools')?.children?.map((ch) => {
                        return (ch as BaseReactUiElement).createReactElement();
                    })}
                </div>
                <div
                    style={{
                        flexGrow: 1,
                        overflowY: 'auto',
                        marginTop: token.paddingXXS,
                    }}
                >
                    {props.element.findByTag('content')?.createReactElement()}
                </div>
            </div>
        </EditorContext.Provider>
    );
}

export class EntityEditorComponent extends EntityEditorSkeleton {
    functionalComponent = EntityEditorFC;
}

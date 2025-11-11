import { RouterContext } from 'admin/src/common/router';
import { useEffect, useState } from 'react';
import { MainRouterSkeleton } from 'admin/src-gen/mainFrame/MainRouterSkeleton';

function MainRouterFC(props: { element: MainRouterComponent }) {
    for (const prop of props.element.state.keys()) {
        const [value, setValue] = useState(props.element.state.get(prop));
        props.element.state.set(prop, value);
        props.element.stateSetters.set(prop, setValue);
    }
    function handlePopEvent() {
        const currentPath = props.element.state.get('path') as string;
        const hasChanges = props.element.state.get('hasChanges') as boolean;
        const message = props.element.state.get('confirmMessage') as string;
        if (hasChanges) {
            if (!window.confirm(message)) {
                window.history.pushState(null, '', currentPath);
                return;
            }
        }
        props.element.navigate({ path: window.location.pathname, force: true });
    }
    useEffect(() => {
        props.element.state.forEach((value, key) => {
            props.element.stateSetters.get(key)?.(value);
        });
        window.addEventListener('popstate', handlePopEvent);
        return () => {
            window.removeEventListener('popstate', handlePopEvent);
        };
    }, [props.element]);
    return (
        <RouterContext.Provider
            value={{
                hasChanges: !!props.element.getHasChanges(),
                setHasChanges: (value) => props.element.setHasChanges(value),
                confirmMessage: props.element.getConfirmMessage(),
                setConfirmMessage: (message) =>
                    props.element.setConfirmMessage(message),
            }}
        >
            {props.element.findByTag('content').createReactElement()}
        </RouterContext.Provider>
    );
}

export class MainRouterComponent extends MainRouterSkeleton {
    functionalComponent = MainRouterFC;

    navigate(value: { path: string; force: boolean }) {
        this.sendCommand('navigate', value, false);
    }

    protected updatePropertyValue(pn: string, pv: any) {
        super.updatePropertyValue(pn, pv);
        if (pn === 'path') {
            window.history.pushState(null, '', pv);
            return;
        }
    }

    setHasChanges(value: boolean) {
        this.state.set('hasChanges', value);
        this.stateSetters.get('hasChanges')!(value);
        this.sendPropertyChange('hasChanges', value, true);
    }

    setConfirmMessage(value?: string) {
        this.state.set('confirmMessage', value);
        this.stateSetters.get('confirmMessage')!(value);
        this.sendPropertyChange('confirmMessage', value, true);
    }
}

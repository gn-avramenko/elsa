import React, { useState, useEffect, ReactNode } from 'react';
import ReactDOM from 'react-dom';
import { BaseReactUiElement } from './common-component';
import { Tabs } from './tabs';

interface ModalProps {
    isOpen: boolean;
    onClose: () => void;
    title?: string;
    children: ReactNode;
}

interface ModalPortalProps {
    children: ReactNode;
}

const ModalPortal: React.FC<ModalPortalProps> = ({ children }) => {
    const [modalRoot, setModalRoot] = useState<HTMLElement | null>(null);

    useEffect(() => {
        const root = document.createElement('div');
        root.id = 'modal-root';
        document.body.appendChild(root);
        setModalRoot(root);

        return () => {
            if (document.body.contains(root)) {
                document.body.removeChild(root);
            }
        };
    }, []);

    if (!modalRoot) return null;

    return ReactDOM.createPortal(children, modalRoot);
};

const AdvancedModal: React.FC<ModalProps> = ({ isOpen, onClose, title, children }) => {
    useEffect(() => {
        const handleEscape = (e: KeyboardEvent) => {
            if (e.key === 'Escape') onClose();
        };

        if (isOpen) {
            document.addEventListener('keydown', handleEscape);
            document.body.style.overflow = 'hidden';
        }

        return () => {
            document.removeEventListener('keydown', handleEscape);
            document.body.style.overflow = 'unset';
        };
    }, [isOpen, onClose]);

    if (!isOpen) return null;

    return (
        <ModalPortal>
            <div className="modal-overlay" onClick={onClose}>
                <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                    <div className="modal-header">
                        {title && <h2>{title}</h2>}
                        <button
                            className="modal-close"
                            onClick={onClose}
                            aria-label="Закрыть диалоговое окно"
                        >
                            ×
                        </button>
                    </div>
                    <div className="modal-body">{children}</div>
                </div>
            </div>
        </ModalPortal>
    );
};

interface DetailsModalProps {
    isOpen: boolean;
    onClose: () => void;
    element: BaseReactUiElement;
}

const toString = (data: Map<string, any | undefined>) => {
    const obj = {} as any;
    data.forEach((value, key) => (obj[key] = value));
    return JSON.stringify(obj, null, 3);
};

export const DetailsModal: React.FC<DetailsModalProps> = ({
    isOpen,
    onClose,
    element,
}) => {
    const initParams = toString(element.initParams);
    const state = toString(element.state);
    const payloadValues = new Map<string, string | undefined>();
    const payloadSetters = new Map<string, any>();
    for (const act of element.actionsFromClient) {
        const [v, setV] = useState<string | undefined>();
        payloadValues.set(act, v);
        payloadSetters.set(act, setV);
    }
    return (
        <AdvancedModal isOpen={isOpen} onClose={onClose} title="Details">
            <Tabs
                tabs={[
                    {
                        id: 'init-props',
                        label: 'Init properties',
                        content: (
                            <textarea
                                name="init-params"
                                disabled
                                value={initParams}
                                style={{ width: '100%', height: '400px' }}
                            />
                        ),
                    },
                    {
                        id: 'state',
                        label: 'State',
                        content: (
                            <textarea
                                name="state"
                                disabled
                                style={{ width: '100%', height: '400px' }}
                                value={state}
                            />
                        ),
                    },
                    {
                        id: 'actions',
                        label: 'Actions',
                        content: (
                            <div
                                key="actions"
                                style={{
                                    width: '100%',
                                    height: '400px',
                                    overflowY: 'auto',
                                }}
                            >
                                {element.actionsFromClient.map((action) => (
                                    <div
                                        key={action}
                                        className="webpeer-action-container"
                                    >
                                        <button
                                            key="button"
                                            className="webpeer-button"
                                            onClick={() =>
                                                element.sendCommand(
                                                    action,
                                                    payloadValues.get(action) == null
                                                        ? null
                                                        : JSON.parse(
                                                              payloadValues.get(action)!
                                                          ),
                                                    false
                                                )
                                            }
                                        >
                                            {action}
                                        </button>
                                        <textarea
                                            key="text-area"
                                            className="webpeer-action-textarea"
                                        ></textarea>
                                    </div>
                                ))}
                            </div>
                        ),
                    },
                ]}
                key="tabs"
                defaultActiveTab="init-props"
            />
        </AdvancedModal>
    );
};

interface MainModalProps {
    isOpen: boolean;
    onClose: () => void;
    content?: BaseReactUiElement;
    buttons?: BaseReactUiElement[];
}

export const MainModal: React.FC<MainModalProps> = ({
    isOpen,
    onClose,
    content,
    buttons,
}) => {
    return isOpen ? (
        <ModalPortal>
            <div className="modal-overlay" onClick={onClose}>
                <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                    <div className="modal-header">
                        <h2>Dialog</h2>
                        <button
                            className="modal-close"
                            onClick={onClose}
                            aria-label="Закрыть диалоговое окно"
                        >
                            ×
                        </button>
                    </div>
                    <div className="modal-body" style={{ height: '420px' }}>
                        {content?.createReactElement() ?? ''}
                    </div>
                    <div className="modal-footer">
                        {buttons?.map((it) => it.createReactElement()) ?? ''}
                    </div>
                </div>
            </div>
        </ModalPortal>
    ) : null;
};

interface UseModalReturn {
    isOpen: boolean;
    open: () => void;
    close: () => void;
}

export const useModal = (): UseModalReturn => {
    const [isOpen, setIsOpen] = useState<boolean>(false);

    const open = (): void => {
        setIsOpen(true);
    };
    const close = (): void => setIsOpen(false);

    return { isOpen, open, close };
};

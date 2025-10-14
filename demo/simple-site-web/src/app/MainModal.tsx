import { BaseReactUiElement, initStateSetters } from '@/common/component';
import { MainModalSkeleton } from '@g/app/MainModalSkeleton';
import { useState } from 'react';
import { NotificationType } from '@g/common/NotificationType';

import { MainModalNotifyAction } from '@g/app/MainModalNotifyAction';
import { NotificationFC } from '@/common/notification';
import { Modal } from '@/common/modal';

function MainModalFC(props: { element: MainModalComponent }) {
    initStateSetters(props.element);
    const [isNotificationActive, setNotificationActive] = useState(false);
    const [notificationMessage, setNotificationMessage] = useState('');
    const [notificationType, setNotificationType] = useState<NotificationType>('INFO');
    const showNotification = (message: string, type: NotificationType) => {
        setNotificationMessage(message);
        setNotificationType(type);
        setNotificationActive(true);
    };
    props.element.processNotify = (notification) =>
        showNotification(notification.message, notification.type);

    return (
        <>
            <NotificationFC
                message={notificationMessage}
                onClose={() => setNotificationActive(false)}
                isOpen={isNotificationActive}
                type={notificationType}
                duration={props.element.getNotificationDuration() ?? 1000}
            />
            <Modal
                isOpen={props.element.getVisible()}
                onClose={() => {
                    props.element.sendClose();
                }}
                title={props.element.getTitle()}
                content={
                    props.element.findByTag('content')?.children?.[0] as
                        | BaseReactUiElement
                        | undefined
                }
                buttons={props.element.findByTag('buttons')?.children as any}
            />
        </>
    );
}
export class MainModalComponent extends MainModalSkeleton {
    functionalComponent = MainModalFC;
    processNotify(_value: MainModalNotifyAction) {}
}

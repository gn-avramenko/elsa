// components/ToastNotification.tsx
import React, { useEffect } from 'react';
import './notification.css';

interface ToastNotificationProps {
    isOpen: boolean;
    onClose: () => void;
    message: string;
    duration?: number;
    type?: 'success' | 'error' | 'info' | 'warning';
}

const ToastNotification: React.FC<ToastNotificationProps> = ({
    isOpen,
    onClose,
    message,
    duration = 2000,
    type = 'info',
}) => {
    useEffect(() => {
        if (!isOpen) return;

        const timer = setTimeout(() => {
            onClose();
        }, duration);

        return () => clearTimeout(timer);
    }, [isOpen, onClose, duration]);

    if (!isOpen) return null;

    const getIcon = () => {
        switch (type) {
            case 'success':
                return '✅';
            case 'error':
                return '❌';
            case 'warning':
                return '⚠️';
            default:
                return '💡';
        }
    };

    const getClassName = () => {
        return `toast-notification toast-${type}`;
    };

    return (
        <div className={getClassName()}>
            <span className="toast-icon">{getIcon()}</span>
            <span className="toast-message">{message}</span>
        </div>
    );
};

export default ToastNotification;

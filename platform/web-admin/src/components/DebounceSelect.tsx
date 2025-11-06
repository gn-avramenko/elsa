import { Select, SelectProps, Spin } from 'antd';
import React, { useMemo, useRef, useState } from 'react';
import debounce from 'lodash/debounce';
export interface DebounceSelectProps<ValueType = any>
    extends Omit<SelectProps<ValueType | ValueType[]>, 'options' | 'children'> {
    fetchOptions: (search?: string) => Promise<ValueType[]>;
    debounceTimeout?: number;
    noContentFoundStr?: string;
    value: ValueType[];
}

export function DebounceSelect<
    ValueType extends {
        key?: string;
        label: React.ReactNode;
        value: string | number;
    } = any,
>({
    fetchOptions,
    value,
    noContentFoundStr,
    debounceTimeout = 300,
    ...props
}: DebounceSelectProps<ValueType>) {
    const [fetching, setFetching] = useState(false);
    const [options, setOptions] = useState<ValueType[]>([]);
    const fetchRef = useRef(0);
    const [open, setOpen] = useState(false);
    const loadOptions = (query?: string) => {
        fetchRef.current += 1;
        const fetchId = fetchRef.current;
        setOptions([]);
        setFetching(true);

        fetchOptions(query).then((newOptions) => {
            if (fetchId !== fetchRef.current) {
                // for fetch callback order
                return;
            }
            const values = value.map((it) => it.value);
            setOptions(newOptions.filter((it) => values.indexOf(it.value) === -1));
            setFetching(false);
        });
    };
    const debounceFetcher = useMemo(() => {
        return debounce(loadOptions, debounceTimeout);
    }, [fetchOptions, debounceTimeout]);

    return (
        <Select
            labelInValue
            filterOption={false}
            open={open}
            value={value}
            onOpenChange={(visible) => {
                setOpen(visible);
                if (visible && !fetching) {
                    loadOptions();
                }
            }}
            onSelect={() => {
                setOpen(false);
            }}
            onSearch={debounceFetcher}
            notFoundContent={fetching ? <Spin size="small" /> : noContentFoundStr}
            {...props}
            options={options}
            optionRender={(option) => (
                <div style={{ display: 'flex', alignItems: 'center' }}>
                    {option.label}
                </div>
            )}
        />
    );
}
